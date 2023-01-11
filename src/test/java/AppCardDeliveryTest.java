import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    private String generateDate (int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldBeSuccessfullyCompleted() {
        open("http://localhost:7777");
        String city = "Санкт-Петербург";
        int daysToAdd = 7;
        int defaultAddedDays = 3;
        $("[data-test-id='city'] input").setValue(city.substring(0,2));
        $$(".menu-item__control").findBy(text(city)).click();
        $("[data-test-id='date'] input").click();
        if(!generateDate(defaultAddedDays, "MM").equals(generateDate(daysToAdd, "MM"))) {
            $("[data-step='1']").click();
        }
        $$(".calendar__day").findBy(text(generateDate(daysToAdd, "d"))).click();
        $("[data-test-id='name'] input").setValue("Иванов Иван Иванович");
        $("[data-test-id='phone'] input").setValue("+78005553535");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " +
                        generateDate(daysToAdd, "dd.MM.yyyy")));

    }
}
