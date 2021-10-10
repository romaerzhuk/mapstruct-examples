package mapper;

import dto.DocumentDto;
import entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * DocumentMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper
public abstract class DocumentMapper {
    @Mapping(target = "status.system", source = "status")
    public abstract DocumentDto toDto(Document entity);

    @Mapping(target = "status", source = "status.system")
    public abstract Document toEntity(DocumentDto dto);
}
