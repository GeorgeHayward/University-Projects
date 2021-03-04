import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;


public class InputPlayer implements Player {

	@Override
	public int chooseNum(TreeSet<Integer> avail) {
		Object[] objectArray = avail.toArray();
		Scanner in = new Scanner(System.in);
		System.out.print("Which number do you choose? ");
		System.out.println("The choices are:");
		System.out.println(Arrays.toString(objectArray));
		int choice = in.nextInt();
		return choice;
	}
}
