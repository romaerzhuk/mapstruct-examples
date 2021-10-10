package mapper;

import dto.AccountDto;
import entity.Account;
import org.mapstruct.Mapper;

/**
 * AccountMapper.
 *
 * @author Roman_Erzhukov
 */
@Mapper
public interface AccountMapper {
    AccountDto toDto(Account entity);

    Account toEntity(AccountDto dto);
}
