import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * The ReplayFileFilter class is used to only allow the user to select .sgf
 * files to open
 * 
 * @author Chris Hartung
 *
 */
public class ReplayFileFilter extends FileFilter {

    /**
     * This method only allows the user to select directories or .sgf files.
     */
    @Override
    public boolean accept(File f) {
	if (f.isDirectory()) {
	    return true;
	}
	String filename = f.getName();
	return filename.endsWith(".sgf");
    }

    /**
     * This method tells the user to select a .sgf file.
     */
    @Override
    public String getDescription() {
	return "Smart Game Format (.sgf) files";
    }

}
