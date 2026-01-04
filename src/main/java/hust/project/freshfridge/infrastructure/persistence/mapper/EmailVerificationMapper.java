package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.EmailVerification;
import hust.project.freshfridge.infrastructure.persistence.model.EmailVerificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailVerificationMapper {
    EmailVerification toDomain(EmailVerificationModel model);
    EmailVerificationModel toModel(EmailVerification entity);
}
