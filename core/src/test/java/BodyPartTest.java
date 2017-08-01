import com.nukesz.game.BodyPart;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BodyPartTest {

    @Test
    public void testConstructorSuccees() {
        BodyPart bp = new BodyPart(21, 2);

        assertEquals(21, bp.x);
        assertEquals(2, bp.y);
    }
}
