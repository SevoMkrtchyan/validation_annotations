import dto.CustomerDto;
import validator.DtoValidator;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setName("Poxos");
        customerDto.setBirthDay(LocalDate.of(2018, 10, 3));
        customerDto.setEmail("poxosmail.com");
        customerDto.setDiscountRate(1000);

        List<String> strings = DtoValidator.validateDto(customerDto);
        if (!strings.isEmpty()) {
            for (String message : strings) {
                System.out.println(message);
            }
        }

    }

}
