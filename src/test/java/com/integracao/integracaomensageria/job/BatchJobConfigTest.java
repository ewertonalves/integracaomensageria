package com.integracao.integracaomensageria.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;

import com.integracao.integracaomensageria.client.DistritosSpClient;
import com.integracao.integracaomensageria.model.Distritos;

@ExtendWith(MockitoExtension.class)
public class BatchJobConfigTest {
	
	@Mock
    private DistritosSpClient distritosSpClient;
	
	@Mock
    private AmqpTemplate amqpTemplate;

    @InjectMocks
    private BatchJobConfig batchJobConfig;

    private List<Distritos> distritosList;
    
    @BeforeEach
    public void setUp() {
        distritosList = Arrays.asList(new Distritos(1L, "Centro"), new Distritos(2L, "Lapa"));
    }

    @Test
    public void testDistritosReader() {
        when(distritosSpClient.getDistritos()).thenReturn(distritosList);
        ItemReader<Distritos> reader = batchJobConfig.distritosReader();
        assertEquals(distritosList, ((ListItemReader<Distritos>) reader).read());
    }
    
    @Test
    public void testDistritosProcessor() throws Exception {
        Distritos input = new Distritos(1, "Centro");

        ItemProcessor<Distritos, Distritos> processor = new BatchJobConfig(null, null, null, null).distritosProcessor();
        Distritos output = processor.process(input);

        assertEquals(input, output);
    }
    
    @Test
    public void testStepDistritos() throws Exception {
        when(distritosSpClient.getDistritos()).thenReturn(Arrays.asList(new Distritos(1, "Centro")));

        Step step = batchJobConfig.stepDistritos();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        step.execute(stepExecution);

        assertEquals("COMPLETED", stepExecution.getExitStatus().getExitCode());
    }
    

    @Test
    public void testDistritosWriter() throws Exception {
        ItemWriter<Distritos> writer = batchJobConfig.distritosWriter();

        for (Distritos distrito : distritosList) {
            Chunk<Distritos> chunk = new Chunk<>(Arrays.asList(distrito));
            writer.write(chunk);
        }

        verify(amqpTemplate, times(distritosList.size())).convertAndSend("distritosQueue",  any(Distritos.class));
    }


}
