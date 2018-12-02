package com.jgcvi.revolut;

import com.jgcvi.revolut.dao.Message;
import com.jgcvi.revolut.dao.Transfer;
import com.jgcvi.revolut.service.MainService;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/heartbeat", ctx -> ctx.json(new Message("beat")));

        app.put("/createuser/:name/:balance", ctx -> ctx.json(
                MainService.createUser(
                        ctx.pathParam("name"),
                        Double.parseDouble(ctx.pathParam("balance"))))
        );

        app.post("/transfer/:from/:to/:amount", ctx -> ctx.json(
            MainService.transfer(new Transfer(
                    Integer.parseInt(ctx.pathParam("from")),
                    Integer.parseInt(ctx.pathParam("to")),
                    Double.parseDouble(ctx.pathParam("amount")))))
        );
    }
}
