package mapper;

import dto.AccountDto;
import entity.Account;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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

    List<AccountDto> toDtoList(List<Account> entities);

    Account toEntity(AccountDto dto);

    List<Account> toEntities(List<AccountDto> entities);
}
