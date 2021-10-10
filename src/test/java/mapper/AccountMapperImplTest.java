package mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static test.extension.UidExtension.uidL;
import static test.extension.UidExtension.uidS;

import dto.AccountDto;
import entity.Account;
import entity.AccountService;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.extension.UidExtension;
import test.hamcrest.PropertiesMatcher;

import java.util.List;
import java.util.stream.Stream;

/**
 * AccountMapperImplTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith({
        MockitoExtension.class,
        UidExtension.class
})
@SuppressWarnings("unchecked")
class AccountMapperImplTest {

    @InjectMocks
    AccountMapperImpl subj;

    @Spy
    @SuppressWarnings("unused")
    AccountMapperImpl_ delegate;

    @Mock
    AccountService service;

    @ParameterizedTest
    @MethodSource("toDtoArguments")
    void toDto(Account expected) {
        String userName = uidS();
        lenient().doReturn(userName).when(service).userName(expected);

        AccountDto actual = subj.toDto(expected);

        assertThat(actual, accountDto(expected, userName));
        verify(service, times(expected == null ? 0 : 1)).userName(any());
    }

    static Stream<Account> toDtoArguments() {
        return Stream.of(null,
                new Account(),
                newAccount());
    }

    @Test
    void toDtoList() {
        var account1 = newAccount();
        var account2 = newAccount();
        String userName = uidS();
        doReturn(userName).when(service).userName(account1);

        List<AccountDto> actual = subj.toDtoList(List.of(account1, account2));

        assertThat(actual, contains(accountDto(account1, userName), accountDto(account2, null)));
    }

    @Test
    void toDtoList_null() {
        assertThat(subj.toDtoList(null), nullValue());
    }

    Matcher<AccountDto> accountDto(Account expected, String userName) {
        if (expected == null) {
            return nullValue(AccountDto.class);
        }
        return PropertiesMatcher.of(AccountDto.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("bic", actual.getBic(), expected.getBic())
                .add("accountNumber", actual.getAccountNumber(), expected.getAccountNumber())
                .add("userName", actual.getUserName(), userName));
    }

    static Account newAccount() {
        return new Account()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }

    @ParameterizedTest
    @MethodSource("toEntityArguments")
    void toEntity(AccountDto expected) {
        assertThat(subj.toEntity(expected), account(expected));
    }

    static Stream<AccountDto> toEntityArguments() {
        return Stream.of(null,
                new AccountDto(),
                newAccountDto());
    }

    @Test
    void toEntities() {
        var account1 = newAccountDto();
        var account2 = newAccountDto();

        List<Account> actual = subj.toEntities(List.of(account1, account2));

        assertThat(actual, contains(account(account1), account(account2)));
    }

    @Test
    void toEntities_null() {
        assertThat(subj.toEntities(null), nullValue());
    }

    Matcher<Account> account(AccountDto expected) {
        if (expected == null) {
            return nullValue(Account.class);
        }
        return PropertiesMatcher.of(Account.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("bic", actual.getBic(), expected.getBic())
                .add("accountNumber", actual.getAccountNumber(), expected.getAccountNumber()));
    }

    static AccountDto newAccountDto() {
        return new AccountDto()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }
}