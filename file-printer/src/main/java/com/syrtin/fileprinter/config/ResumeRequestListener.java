package com.syrtin.fileprinter.config;

import com.syrtin.fileprinter.dto.ResumePrintRequest;
import com.syrtin.fileprinter.dto.ResumePrintResponse;
import com.syrtin.fileprinter.exception.PdfGenerationException;
import com.syrtin.fileprinter.service.ResumePdfServiceImpl;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResumeRequestListener {

    private final RabbitTemplate rabbitTemplate;

    private final AppProps appProps;

    private final ResumePdfServiceImpl resumePdfService;

    @Timed("PrintRequestInFileProcessor")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${app.rabbitmq.request-queue}", durable = "true"),
            exchange = @Exchange(value = "${app.rabbitmq.exchange}", type = "direct"),
            key = "${app.rabbitmq.request-routing-key}")
    )
    public void handleResumePrintRequest(ResumePrintRequest request, @Header(AmqpHeaders.CORRELATION_ID) String correlationId) {

        ResumePrintResponse response;
        try {
            String downloadLink = resumePdfService.generateResumePdf(request);

            response = new ResumePrintResponse(0L, "Success", downloadLink);
        } catch (PdfGenerationException e) {
            response = new ResumePrintResponse(1L, e.getMessage(), null);
        }

        rabbitTemplate.convertAndSend(appProps.getRabbitmq().getExchange(), appProps.getRabbitmq().getResponseRoutingKey(), response, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });

    }
}
