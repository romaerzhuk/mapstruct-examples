package mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static test.extension.UidExtension.newEnum;
import static test.extension.UidExtension.uidD;
import static test.extension.UidExtension.uidDec;
import static test.extension.UidExtension.uidL;
import static test.extension.UidExtension.uidS;

import dto.AccountDto;
import dto.DocumentDto;
import dto.DocumentStatus;
import dto.StatusDto;
import entity.Account;
import entity.Document;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import test.extension.UidExtension;
import test.hamcrest.PropertiesMatcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * DocumentMapperTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith(UidExtension.class)
class DocumentMapperTest {

    DocumentMapper subj = new DocumentMapperImpl();

    @ParameterizedTest
    @MethodSource("toDtoArguments")
    void toDto(Document expected) {
        assertThat(subj.toDto(expected), documentDto(expected));
    }

    static Stream<Document> toDtoArguments() {
        return Stream.concat(Stream.of(null,
                new Document(),
                newDocument()
                        .setAccounts(null)),
                Stream.of(DocumentStatus.values())
                        .map(status -> newDocument()
                                .setStatus(status)));
    }

    static Document newDocument() {
        return new Document()
                .setId(uidL())
                .setAmount(uidDec())
                .setStatus(newEnum(DocumentStatus.class))
                .setAccounts(List.of(newAccount(), new Account()));
    }

    static Account newAccount() {
        return new Account()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }

    Matcher<DocumentDto> documentDto(Document expected) {
        if (expected == null) {
            return nullValue(DocumentDto.class);
        }
        return PropertiesMatcher.of(DocumentDto.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("amount", actual.getAmount(), expected.getAmount())
                .add("status", actual.getStatus(), expected.getStatus(), (a, e) -> matcher
                        .add("system", a.getSystem(), e)
                        .add("name", a.getName(), name(e)))
                .addList("accounts", actual.getAccounts(), expected.getAccounts(), (a, e) -> matcher
                        .add("id", a.getId(), e.getId())
                        .add("bic", a.getBic(), e.getBic())
                        .add("accountNumber", a.getAccountNumber(), e.getAccountNumber())));
    }

    String name(DocumentStatus status) {
        switch (status) {
            case NEW:
                return "Новый";
            case DRAFT:
                return "Черновик";
            case SIGNED:
                return "Подписан";
            case EXECUTED:
                return "Исполнен";
            default:
                throw new IllegalArgumentException("status=" + status);
        }
    }

    @ParameterizedTest
    @MethodSource("toEntityArguments")
    void toEntity(DocumentDto expected) {
        assertThat(subj.toEntity(expected), document(expected));
    }

    static Stream<DocumentDto> toEntityArguments() {
        return Stream.of(null,
                new DocumentDto(),
                newDocumentDto(),
                newDocumentDto()
                        .setAccounts(null)
                        .setStatus(null)
        );
    }

    static DocumentDto newDocumentDto() {
        return new DocumentDto()
                .setId(uidL())
                .setAmount(uidD())
                .setStatus(new StatusDto()
                        .setSystem(newEnum(DocumentStatus.class))
                        .setName(uidS()))
                .setAccounts(List.of(newAccountDto(), new AccountDto()));
    }

    static AccountDto newAccountDto() {
        return new AccountDto()
                .setId(uidL())
                .setBic(uidS())
                .setAccountNumber(uidS());
    }

    Matcher<Document> document(DocumentDto expected) {
        if (expected == null) {
            return nullValue(Document.class);
        }
        return PropertiesMatcher.of(Document.class, (actual, matcher) -> matcher
                .add("id", actual.getId(), expected.getId())
                .add("amount", actual.getAmount(), expected.getAmount())
                .add("status", actual.getStatus(), Optional.ofNullable(expected.getStatus())
                        .map(StatusDto::getSystem)
                        .orElse(null))
                .addList("accounts", actual.getAccounts(), expected.getAccounts(), (a, e) -> matcher
                        .add("id", a.getId(), e.getId())
                        .add("bic", a.getBic(), e.getBic())
                        .add("accountNumber", a.getAccountNumber(), e.getAccountNumber())));
    }
}