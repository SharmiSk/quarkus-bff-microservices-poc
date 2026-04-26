package com.qurkus.bff.resource;

import com.qurkus.bff.client.AccountClient;
import com.qurkus.bff.client.BudgetClient;
import com.qurkus.bff.client.TransactionClient;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Duration;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    private static final Duration DOWNSTREAM_TIMEOUT = Duration.ofSeconds(2);
    private final Executor executor = Executors.newFixedThreadPool(8);

    @Inject @RestClient AccountClient accountClient;
    @Inject @RestClient TransactionClient transactionClient;
    @Inject @RestClient BudgetClient budgetClient;

    public record SummaryResponse(
            long totalBalanceCents,
            long monthlySpendCents,
            List<TransactionClient.CategorySpend> categoryBreakdown,
            long monthlyBudgetCents,
            List<String> warnings
    ) {}

    @GET
    @Path("/summary")
    @RolesAllowed("USER")
    public SummaryResponse summary(@QueryParam("month") String month, @HeaderParam("Authorization") String authorization) {
        YearMonth ym = (month == null || month.isBlank()) ? YearMonth.now() : YearMonth.parse(month);
        String monthStr = ym.toString();

        List<String> warnings = new ArrayList<>();

        CompletableFuture<AccountClient.BalanceResponse> balanceF =
                CompletableFuture.supplyAsync(() -> accountClient.totalBalance(authorization), executor)
                        .orTimeout(DOWNSTREAM_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .exceptionally(ex -> {
                            warnings.add("Account service unavailable");
                            return new AccountClient.BalanceResponse(0);
                        });

        CompletableFuture<TransactionClient.MonthlySpendResponse> spendF =
                CompletableFuture.supplyAsync(() -> transactionClient.monthlySpend(monthStr, authorization), executor)
                        .orTimeout(DOWNSTREAM_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .exceptionally(ex -> {
                            warnings.add("Transaction service unavailable (monthlySpend)");
                            return new TransactionClient.MonthlySpendResponse(0);
                        });

        CompletableFuture<TransactionClient.CategoryBreakdownResponse> breakdownF =
                CompletableFuture.supplyAsync(() -> transactionClient.categoryBreakdown(monthStr, authorization), executor)
                        .orTimeout(DOWNSTREAM_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .exceptionally(ex -> {
                            warnings.add("Transaction service unavailable (categoryBreakdown)");
                            return new TransactionClient.CategoryBreakdownResponse(List.of());
                        });

        CompletableFuture<BudgetClient.MonthlyBudgetResponse> budgetF =
                CompletableFuture.supplyAsync(() -> budgetClient.monthlyBudget(monthStr, authorization), executor)
                        .orTimeout(DOWNSTREAM_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                        .exceptionally(ex -> {
                            warnings.add("Budget service unavailable");
                            return new BudgetClient.MonthlyBudgetResponse(0);
                        });

        CompletableFuture.allOf(balanceF, spendF, breakdownF, budgetF).join();

        return new SummaryResponse(
                balanceF.join().totalBalanceCents(),
                spendF.join().monthlySpendCents(),
                breakdownF.join().categories(),
                budgetF.join().budgetCents(),
                warnings
        );
    }
}

