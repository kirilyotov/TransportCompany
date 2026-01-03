package org.example.mapper;

import org.example.dao.ClientDao;
import org.example.dao.TransportDao;
import org.example.dto.PaymentDto;
import org.example.entity.Payment;
import org.example.entity.Transport;
import org.example.entity.Client;

public final class PaymentMapper implements EntityMapper<Payment, PaymentDto> {
    
    private final TransportDao transportDao;
    private final ClientDao clientDao;
    
    public PaymentMapper(TransportDao transportDao, ClientDao clientDao) {
        this.transportDao = transportDao;
        this.clientDao = clientDao;
    }
    
    @Override
    public Payment toEntity(PaymentDto dto) {
        Payment payment = Payment.builder()
                .amount(dto.amount())
                .paymentDateTime(dto.paymentDateTime())
                .status(dto.status())
                .build();
        
        if (dto.id() != null) {
            payment.setId(dto.id());
        }
        
        if (dto.transportId() != null) {
            Transport transport = transportDao.findById(dto.transportId());
            payment.setTransport(transport);
        }
        
        if (dto.clientId() != null) {
            Client client = clientDao.findById(dto.clientId());
            payment.setClient(client);
        }
        
        return payment;
    }
    
    @Override
    public PaymentDto toDto(Payment entity) {
        return new PaymentDto(
                entity.getId(),
                entity.getTransport() != null ? entity.getTransport().getId() : null,
                entity.getClient() != null ? entity.getClient().getId() : null,
                entity.getAmount(),
                entity.getPaymentDateTime(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
