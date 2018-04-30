package workstation;

import java.util.ArrayList;
import java.util.List;


public class searchNotes 
{
    
    public List<Notes> searchKeyword(List<List<Notes>> sList, CharSequence cs)
    {
        List<Notes> kHold = new ArrayList<>();
        for(List<Notes> note : sList)
        {
            for(Notes internalNote : note)
            {
                if(internalNote.getContent().contains(cs))
                    kHold.add(internalNote);
            }
        }
        return kHold;
    }
    
    public List<Notes> searchTitle(List<List<Notes>> sList, String title)
    {
        List<Notes> hold = new ArrayList<>();
        for(List<Notes> note : sList)
        {
            for(Notes internalNote : note)
            {
                if(internalNote.getTitle().equalsIgnoreCase(title))
                    hold.add(internalNote);
            }
        }
        return hold;
    }
    
}
