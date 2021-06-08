package pl.trollcraft.Skyblock.top;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;

public class Top {

    private String header;
    private Location location;
    private Hologram hologram;

    public Top(String header, Location location, Hologram hologram){
        this.header = header;
        this.location = location;
        this.hologram = hologram;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }
}
