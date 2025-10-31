package io.github.tessla2.libraryapi.repository;

import io.github.tessla2.libraryapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TransacoesTest {

    @Autowired
    TransactionService transacaoService;

    /**
     * Commit -> confirmar as alterações
     * Rollback -> desfazer as alterações
     */
    @Test
    void transacaoSimples(){
        transacaoService.execute();
    }

    @Test
    void transacaoEstadoManaged(){
        transacaoService.updateWithoutSave();
    }
}