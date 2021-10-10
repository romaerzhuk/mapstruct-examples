package mapper;

import dto.AccountDto;
import entity.Account;
import entity.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AccountMapperDecorator.
 *
 * @author Roman_Erzhukov
 */
public abstract class AccountMapperDecorator implements AccountMapper {

    @Qualifier("delegate")
    @Autowired
    protected AccountMapper delegate;

    @Autowired
    private AccountService service;

    @Override
    public AccountDto toDto(Account entity) {
        AccountDto dto = delegate.toDto(entity);
        if (dto == null) {
            return null;
        }
        return dto.setUserName(service.userName(entity));
    }

    @Override
    public List<AccountDto> toDtoList(List<Account> entities)  {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Account toEntity(AccountDto dto) {
        return delegate.toEntity(dto);
    }

    @Override
    public List<Account> toEntities(List<AccountDto> entities)  {
        return delegate.toEntities(entities);
    }
}
