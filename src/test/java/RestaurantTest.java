import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.*;
import java.time.LocalTime;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class RestaurantTest {
    Restaurant restaurant;

    @BeforeEach
    public void BeforeEach() {
        LocalTime _open = LocalTime.parse("10:30:00");
        LocalTime _close = LocalTime.parse("22:00:00");
        Restaurant _restaurant = new Restaurant( "Amelie's cafe", "Chennai", _open, _close);
        restaurant = Mockito.spy(_restaurant);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    @AfterEach
    public void afterEach() {
        Mockito.reset(restaurant);
        try {
            restaurant.removeFromMenu("Sweet corn soup");
            restaurant.removeFromMenu("Vegetable lasagne");
            restaurant.removeFromMenu("Sizzling brownie");
        } catch (itemNotFoundException ex) {
            return;
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        Mockito.when(restaurant.getCurrentTime()).thenReturn(LocalTime.parse("11:00:00"));
        assertTrue(restaurant.isRestaurantOpen());
        Mockito.reset(restaurant);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        Mockito.when(restaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:29:00"));
        assertFalse(restaurant.isRestaurantOpen());

        Mockito.when(restaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:30:00"));
        assertFalse(restaurant.isRestaurantOpen());

        Mockito.when(restaurant.getCurrentTime()).thenReturn(LocalTime.parse("22:00:00"));
        assertFalse(restaurant.isRestaurantOpen());

        Mockito.when(restaurant.getCurrentTime()).thenReturn(LocalTime.parse("22:01:00"));
        assertFalse(restaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Test
    public void get_total_price_should_total_price_for_valid_item_name_list() {
        List<String> _list = new ArrayList<String>();
        _list.add("Sweet corn soup");
        _list.add("Vegetable lasagne");

        int _total = 119 + 269;
        assertEquals(restaurant.getTotal(_list), _total);

        restaurant.addToMenu("Sizzling brownie", 319);
        _list.add("Sizzling brownie");
        _total = 119 + 269 + 319;
        assertEquals(restaurant.getTotal(_list), _total);
    }
}