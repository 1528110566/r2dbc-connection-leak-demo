package org.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author Tao Zheng
 * @date 2024-11-23 16:34
 */
@RestController
public class DemoController {
    @Autowired
    private R2dbcEntityTemplate template;
    @Autowired
    private R2dbcTransactionManager transactionManager;
    @Autowired
    private TransactionalOperator transactionalOperator;

    @GetMapping("/test")
    public String test() {
        Query query = Query.query(Criteria.where("id").is(3));
        Update update = Update.update("name", "3");
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        for (int i = 0; i < 1000; i++) {
            Flux.fromIterable(list)
                    .flatMap(item -> {
                        return transactionalOperator.transactional(template.update(query, update, User.class)
                                .doOnNext(res -> {
                                    if (res != 1) {
                                        throw new RuntimeException("test");
                                    }
                                }));
                    })
                    .subscribe();
        }


        return "ok";
    }

    @Data
    public static class User implements Serializable {
        private Integer id;
        private String name;
    }
}
