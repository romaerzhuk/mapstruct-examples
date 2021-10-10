package mapper;

import dto.AccountDto;
import entity.Account;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * AccountMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper
@DecoratedWith(AccountMapperDecorator.class)
public interface AccountMapper {
    @Mapping(target = "userName", ignore = true)
    AccountDto toDto(Account entity);

    Account toEntity(AccountDto dto);
}
