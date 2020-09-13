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
import java.util.concurrent.ThreadLocalRandom;

public class Ball {
    private double[] position;
    private int speed; 
    private int radius;
    private int status; // -1 = susceptible, 0 = infected, 1 = recovered
    private Random r = new Random();
    private double dimension; // Assume in an n-dimensional cube
    private int daysSinceInfected;
    private int infectDays;
    
    public Ball(double[] position, int v, int radius, int status, int d) {
        this.position = position;
        this.speed = v;
        this.radius = radius;
        this.status = status;
        this.daysSinceInfected = -1;
        if (this.status == 0) {
            this.daysSinceInfected = 0;
        }
        
        int minDays = 5;
        int maxDays = 14;
        this.infectDays = minDays + r.nextInt(maxDays - minDays + 1);
        this.dimension = d;
    }
    
    public void updatePosition() {
        double speedLocal = (double)this.speed;
        
        for (int i = 0; i < this.position.length; i++) {
            double dx = speedLocal * r.nextDouble();
            if (i == this.position.length - 1) {
                dx = speedLocal;
            }
            if (ThreadLocalRandom.current().nextInt(0, 2) == 1) {
                dx = -dx;
            }

            this.position[i] += dx;
            if (this.position[i] > this.dimension) {
                this.position[i] = (2 * this.dimension) - this.position[i];
            }
            else if (this.position[i] < 0) {
                this.position[i] = -this.position[i];
            }
            
            speedLocal = Math.sqrt((speedLocal * speedLocal) - (dx * dx));
        }
    }
    
    public int getStatus() { return this.status; }
    private void setStatus(int status) { this.status = status; }
    public int getRadius() { return this.radius; }
    public double[] getPosition() { return this.position; }
    
    public int daysSinceInfected() { return this.daysSinceInfected; }
    public void setDaysSinceInfected(int days) { this.daysSinceInfected = days; }
    
    public void recover() {
        if (this.daysSinceInfected >= this.infectDays && this.status == 0) { 
            this.status = 1;
            this.daysSinceInfected = -1;
        }
        else if (this.daysSinceInfected >= 0 && this.status == 0) {
            this.daysSinceInfected++;
        }
    }
    
    public double getDistance(Ball b) {
        double[] otherPosition = b.getPosition();
        double distance = 0.0;
        for (int i = 0; i < this.position.length; i++) {
            distance += (this.position[i] - otherPosition[i]) * 
                        (this.position[i] - otherPosition[i]);
        }
        return Math.sqrt(distance);
    }
    
    
    public void updateStatus(Ball b) {
        if (this.getDistance(b) <= (b.getRadius() + this.radius)) {
            if (b.getStatus() == 0 && this.status == -1) {
                this.setStatus(0);
                this.daysSinceInfected = 0;
            }
            else if (b.getStatus() == -1 && this.status == 0) {
                b.setStatus(0);
                b.setDaysSinceInfected(0);
            }
        }
    }
}
