package com.kreig133.sveta;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class Generator {

    List<Measurment> measurments = new LinkedList<Measurment>();

    double currentTime;
    double nextMeasurement;
    double startNoise;
    double endNoise;
    double amplitude;

    double maxTime;

    private RGauss gTemp;
    private RExponenta gDuration;
    private RGauss gAmplitude;
    private RGauss gFrequency;

    long withNoiseCount = 0;

    public Generator( double  msxTime ) {
        this.maxTime = msxTime;

        gTemp = new RGauss( 900.0, 50.0 );
        gDuration = new RExponenta( 0.35, 0.050 );
        gAmplitude = new RGauss( 400.0, 400.0 );
        gFrequency = new RGauss( 1500.0, 1400.0 );
    }

    public void run(){
        amplitude = gAmplitude.next();

        while ( currentTime < maxTime ){
            if (  endNoise <= currentTime ) {
                startNoise = currentTime + ( 1000.0 / gFrequency.next() );
                amplitude = gAmplitude.next();
                endNoise = startNoise + gDuration.next();
            }

            if ( nextMeasurement <= currentTime ) {
                nextMeasurement += 50.0;

                final boolean withNoise = ( startNoise <= currentTime ) && ( currentTime <= endNoise );

                final Measurment measurment = new Measurment(
                        currentTime,
                        withNoise ? gTemp.next() + amplitude : gTemp.next(),
                        withNoise
                );
                if( withNoise ){
                    withNoiseCount++;
                }

                measurments.add( measurment );

//                System.out.println( measurment );
            }

            currentTime = ( endNoise < nextMeasurement ) ? endNoise : nextMeasurement;
        }
        
        saveToFile();
    }

    private void saveToFile() {
        try {
            final File file = new File( "data.csv" );
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter( file );
            for ( Measurment measurment : measurments ) {
                fileWriter.write(
                        measurment.time + ";" + measurment.temp + ";" + ( measurment.withNoise ? "0" : "1" ) + ";\n"
                );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        final int msxTime = 10000000;
        final Generator generator = new Generator( msxTime );

        generator.run();

        System.out.println();
        System.out.println( (double)generator.withNoiseCount / (double)generator.measurments.size() );
    }
}
