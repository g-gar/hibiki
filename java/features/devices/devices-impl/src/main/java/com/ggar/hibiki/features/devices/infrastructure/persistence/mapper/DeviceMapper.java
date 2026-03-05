package com.ggar.hibiki.features.devices.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.devices.domain.model.Device;
import com.ggar.hibiki.features.devices.infrastructure.persistence.entity.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeviceMapper {

    DeviceEntity toEntity(Device device);

    Device toDomain(DeviceEntity entity);
}
