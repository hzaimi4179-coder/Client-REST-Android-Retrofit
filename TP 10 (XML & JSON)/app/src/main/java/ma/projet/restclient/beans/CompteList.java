package ma.projet.restclient.beans;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "list", strict = false)
public class CompteList {
    
    @ElementList(inline = true, required = false)
    private List<Compte> comptes;

    public CompteList() {
    }

    public CompteList(List<Compte> comptes) {
        this.comptes = comptes;
    }

    public List<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }
}
