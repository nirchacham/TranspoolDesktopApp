package checks;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

import classes.TranspoolSystem;
import exceptions.*;
import schema.*;

public class FileCheck {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "schema";
    private String path;
    private InputStream inputStream;
    private TransPool transPool;

    public FileCheck(String path) {
        this.path=path;
    }

    public TransPool getTransPool() {
        return transPool;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void checkPathName() throws WrongExtension, PathIsToShort {
        if(path.length()>4)
        {
            String extension = path.substring(path.length() - 4);
            if(!extension.equals(".xml")) {
                throw new WrongExtension();
            }
        }
        else {   //path<4
            throw new PathIsToShort();
        }
    }

    public void openXmlFile() throws PathNotFound, FileNotFoundException {
        //inputStream = FileCheck.class.getResourceAsStream(path);
        inputStream = new FileInputStream(path);
        if(inputStream==null) {
            throw new PathNotFound();
        }
    }

    public void createTranspool() throws JAXBException {        //what happens if id doesnt succeed
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        transPool=(TransPool) u.unmarshal(inputStream);
    }

    public void checkMapBoundaries() throws JAXBException, MapBoundariesInvalid {
        MapBoundries mapBoundries=transPool.getMapDescriptor().getMapBoundries();
        int length=mapBoundries.getLength();
        int width=mapBoundries.getWidth();
            if (length < 6 || length > 100 || width < 6 || width > 100) {
                throw new MapBoundariesInvalid();
            }
    }

    public void checkUniqueName() throws JAXBException, NotUniqueName {
        Stops stops=transPool.getMapDescriptor().getStops();
        List<Stop> stopList=stops.getStop();
        for(int i=0; i<stopList.size(); i++) {
            for(int j=i+1; j<stopList.size(); j++) {
                if (stopList.get(i).getName().trim().equals(stopList.get(j).getName().trim())) {
                    throw new NotUniqueName(stopList.get(i).getName().trim());
                }
            }
        }
    }

    public void checkStopInBoundaries() throws JAXBException, StopBoundaries {
        MapBoundries mapBoundries=transPool.getMapDescriptor().getMapBoundries();
        int length=mapBoundries.getLength();
        int width=mapBoundries.getWidth();
        Stops stops=transPool.getMapDescriptor().getStops();
        List<Stop> stopList=stops.getStop();
        for(Stop stop : stopList) {
            if(stop.getX()<0 || stop.getX()>width || stop.getY()<0 || stop.getY()>length) {
                throw new StopBoundaries(stop.getName(),length,width);
            }
        }
    }

    public void checkUniqueLocation() throws JAXBException, NotUniqueLocation {
        Stops stops=transPool.getMapDescriptor().getStops();
        List<Stop> stopList=stops.getStop();
        for(int i=0; i<stopList.size(); i++) {
            for (int j = i + 1; j < stopList.size(); j++) {
                if (stopList.get(i).getX() == stopList.get(j).getX() && stopList.get(i).getY() == stopList.get(j).getY()) {
                    throw new NotUniqueLocation(stopList.get(i).getName(), stopList.get(j).getName());
                }
            }
        }
    }

    public void checkPathExistence() throws JAXBException, PathAlreadyExists {
        Stops stops=transPool.getMapDescriptor().getStops();
        List<Stop> stopList=stops.getStop();
        Paths paths=transPool.getMapDescriptor().getPaths();
        List<Path> pathList=paths.getPath();
        boolean foundFrom=false, foundTo=false;
        for(Path path:pathList) {
            for(Stop stop:stopList) {
                if (path.getFrom().trim().equals(stop.getName().trim())) {
                    foundFrom = true;
                }
                if (path.getTo().trim().equals(stop.getName().trim())) {
                    foundTo = true;
                }
                if (foundFrom && foundTo) {
                    break;
                }
            }
            if(!foundFrom || !foundTo) {
                throw new PathAlreadyExists(path.getFrom(),path.getTo());
            }
            foundFrom=foundTo=false;
        }
    }

    public void checkRoutes() throws JAXBException, InvalidRoute {
        PlannedTrips plannedTrips=transPool.getPlannedTrips();
        List<TransPoolTrip> trips=plannedTrips.getTransPoolTrip();
        Paths paths=transPool.getMapDescriptor().getPaths();
        List<Path> pathList=paths.getPath();
        String route, currFrom, currTo;
        boolean found=false;
        int counter=0;
        for(TransPoolTrip trip:trips) {
            route=trip.getRoute().getPath();
            StringTokenizer st = new StringTokenizer(route,",");
            currFrom=null;
            currTo=st.nextToken().trim();
            while(st.hasMoreTokens()) {
                currFrom=currTo;
                currTo=st.nextToken().trim();
                for(Path path:pathList) {
                    if(currFrom.equals(path.getFrom().trim()) && currTo.equals(path.getTo().trim()) ) {
                        found=true;
                        break;
                    }
                    if(currFrom.equals(path.getTo().trim()) && currTo.equals(path.getFrom().trim()) && path.isOneWay()==false) {
                        found=true;
                        break;
                    }
                }
                if(!found) {
                   throw new InvalidRoute(currFrom, currTo);
                }
                found=false;
            }
        }
    }

    public void runAllChecks() throws JAXBException, WrongExtension, PathIsToShort, PathNotFound, MapBoundariesInvalid, NotUniqueName, StopBoundaries, NotUniqueLocation, PathAlreadyExists, InvalidRoute, FileNotFoundException {
        checkPathName();
        openXmlFile();
        createTranspool();
        checkMapBoundaries();
        checkUniqueName();
        checkStopInBoundaries();
        checkUniqueLocation();
        checkPathExistence();
        checkRoutes();
    }
}


