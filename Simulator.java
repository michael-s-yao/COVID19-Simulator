/*******************************************************************************
 * Copyright 2020 Michael S. Yao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
import java.util.Random;

public class Simulator {
    private static Ball[] createBalls(int numBalls, int dimension, 
            int numDimensions, int maxVelocity, int maxRadius, int initInfect) {
        
        Random r = new Random();
        
        Ball[] balls = new Ball[numBalls];
        for (int i = 0; i < balls.length; i++) {
            double[] position = new double[numDimensions];
            for (int j = 0; j < numDimensions; j++) {
                position[j] = dimension * r.nextDouble();
            }
            
            int status = -1;
            if (i < initInfect) { status = 0; }
            
            balls[i] = new Ball(position, (int)(maxVelocity * r.nextDouble()), 
                    (int)(maxRadius * r.nextDouble()), status, dimension);
        }
        
        return balls;
    }
    
    private static void timeStep (Ball[] balls) {
        for (Ball b : balls) {
            b.updatePosition();
            b.recover();
        }
        
        for (int i = 0; i < balls.length; i++) {
            for (int j = 0; j < balls.length; j++) {
                if (i != j) {
                    balls[i].updateStatus(balls[j]);
                }
            }
        }
    }
    
    private static int[] statistics(Ball[] balls) {
        int[] stats = new int[3];
        for (int i = 0; i < stats.length; i++) { stats[i] = 0; }
        
        for (Ball b : balls) {
            stats[b.getStatus() + 1] += 1;
        }
        
        return stats;
    }
    
    public static void main(String[] args) {
        int numBalls = 500;
        int dimension = 500;
        int numDimensions = 5;
        int maxVelocity = 100;
        int timeSteps = 100;
        int maxRadius = 25;
        int initInfect = 5;
        Ball[] balls = createBalls(numBalls, dimension, numDimensions, 
                                    maxVelocity, maxRadius, initInfect);
        int[] susceptible = new int[timeSteps];
        int[] infected = new int[timeSteps];
        int[] recovered = new int[timeSteps];
                
        for (int i = 0; i < timeSteps; i++) {
            int[] stats = statistics(balls);
            susceptible[i] = stats[0];
            infected[i] = stats[1];
            recovered[i] = stats[2];

            timeStep(balls);
        }
        
        for (int i = 0; i < infected.length; i++) {
            System.out.println(infected[i]);
        }
    }
}
