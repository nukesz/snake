import com.nukesz.game.BodyPart;
import org.junit.Assert;
import org.junit.Test;

public class BodyPartTest {

    @Test
    public void test() {
        BodyPart bp = new BodyPart(21, 2);

        Assert.assertEquals(21, bp.x);
        Assert.assertEquals(2, bp.y);
    }
}
