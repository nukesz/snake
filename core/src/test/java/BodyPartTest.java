import com.nukesz.game.BodyPart;
import org.junit.Assert;
import org.junit.Test;

public class BodyPartTest {

    @Test
    public void test() {
        //GIVEN
        BodyPart bp = new BodyPart(21, 2);

        //WHEN/THEN
        Assert.assertEquals(21, bp.getX());
        Assert.assertEquals(2, bp.getY());
    }
}
