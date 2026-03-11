package com.ggar.hibiki.features.devices.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.devices.infrastructure.persistence.entity.DeviceEntity;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.UserId;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeviceMapper {

    DeviceEntity toEntity(Device device);

    Device toDomain(DeviceEntity entity);

    default UUID map(DeviceId value) {
        return value != null ? value.getValue() : null;
    }

    default DeviceId mapDeviceId(UUID value) {
        return value != null ? DeviceId.of(value) : null;
    }

    default UUID map(UserId value) {
        return value != null ? value.getValue() : null;
    }

    default UserId mapUserId(UUID value) {
        return value != null ? UserId.of(value) : null;
    }
}
