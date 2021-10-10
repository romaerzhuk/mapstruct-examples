package mapper;

import dto.DocumentDto;
import entity.Document;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * DocumentMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper(uses = AccountMapper.class)
@DecoratedWith(DocumentMapperDecorator.class)
public interface DocumentMapper {
    @Mapping(target = "status", ignore = true)
    DocumentDto toDto(Document entity);

    @Mapping(target = "status", source = "status.system")
    Document toEntity(DocumentDto dto);
}
