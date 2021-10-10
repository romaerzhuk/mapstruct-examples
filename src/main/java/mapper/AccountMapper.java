package mapper;

import dto.AccountDto;
import entity.Account;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * AccountMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper(componentModel = "spring")
@DecoratedWith(AccountMapperDecorator.class)
public interface AccountMapper {
    AccountDto toDto(Account entity);

    Account toEntity(AccountDto dto);
}
