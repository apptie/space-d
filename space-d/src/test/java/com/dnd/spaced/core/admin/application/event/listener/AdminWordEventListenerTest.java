package com.dnd.spaced.core.admin.application.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.spaced.core.admin.application.event.dto.DeletedWordEvent;
import com.dnd.spaced.core.word.application.DeletedWordIdRepository;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminWordEventListenerTest {

    @Autowired
    AdminWordEventListener adminWordEventListener;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    WordExampleRepository wordExampleRepository;

    @Autowired
    DeletedWordIdRepository deletedWordIdRepository;

    @Autowired
    PronunciationRepository pronunciationRepository;

    @Test
    void 용어_삭제_이벤트_리스너는_트랜잭션이_없다면_이벤트_후처리를_할_수_없다() {
        // given
        DeletedWordEvent event = new DeletedWordEvent(1L);

        // when & then
        assertThatThrownBy(() -> adminWordEventListener.listen(event))
                .isInstanceOf(IllegalTransactionStateException.class);
    }

    @Test
    void 용어_삭제_이벤트를_후처리_한다() {
        // given
        DeletedWordEvent event = new DeletedWordEvent(1L);

        // when
        transactionTemplate.executeWithoutResult(status -> adminWordEventListener.listen(event));

        // then
        assertAll(
                () -> verify(platformTransactionManager, times(2)).getTransaction(any(TransactionDefinition.class)),
                () -> verify(platformTransactionManager).getTransaction(
                        argThat(transactionDefinition -> transactionDefinition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED)
                ),
                () -> verify(platformTransactionManager).getTransaction(
                        argThat(transactionDefinition -> transactionDefinition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY)
                ),
                () -> verify(platformTransactionManager, times(2)).commit(any(TransactionStatus.class)),
                () -> verify(platformTransactionManager).commit(
                        argThat(transactionStatus -> "com.dnd.spaced.core.admin.application.event.listener.AdminWordEventListener.listen".equals(transactionStatus.getTransactionName()) && !transactionStatus.isNewTransaction())
                ),
                () -> verify(platformTransactionManager).commit(
                        argThat(transactionStatus -> transactionStatus.getTransactionName().isEmpty() && transactionStatus.isNewTransaction())
                ),
                () -> verify(platformTransactionManager, never()).rollback(any(TransactionStatus.class)),
                () -> verify(wordExampleRepository).deleteAllBy(anyLong()),
                () -> verify(pronunciationRepository).deleteAllBy(anyLong()),
                () -> verify(deletedWordIdRepository).save(anyLong(), any(LocalDateTime.class))
        );
    }
}
