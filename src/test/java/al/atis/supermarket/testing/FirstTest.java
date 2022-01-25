package al.atis.supermarket.testing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FirstTest {
    private final Calculator calculator = new Calculator();

    @Test
    void addition(){
        int num1 = 2;
        int num2 = 2;
        int result = calculator.add(num1,num2);
        assertThat(result).isEqualTo(4);
    }

    class Calculator {
        int add(int a , int b){
            return a + b;
        }
    }
}
