import java.io.File;
import javax.swing.filechooser.FileFilter;

/*
 * The ReplayFileFilter class is based on the following:
 * 
 * Title: ArffFilter
 * Author: makata 
 * Date: 2015 
 * Availability:
 * https://stackoverflow.com/questions/19302029/filter-file-types-with-jfilechooser
 */
/**
 * The ReplayFileFilter class is used to only allow the user to select .sgf
 * files.
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
	return filename.toLowerCase().endsWith(".sgf");
    }

    /**
     * This method tells the user to select a .sgf file.
     */
    @Override
    public String getDescription() {
	return "Smart Game Format (.sgf) files";
    }

}
