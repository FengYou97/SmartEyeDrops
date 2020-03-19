import sys
import numpy as np
import pandas as pd

df = pd.read_csv("test1.csv")
x = df['time']
y = df['force']

x[0::30]
counter = 0
length = 0
threshold = 700

for i in range(len(x)):
    if y[i] > threshold:
        length += 1
    else:
        if length > 25:
            counter += 1

        length = 0

print("Number of eye drops taken:")
print(counter)
x_acc = df['x']
y_acc = df['y']
z_acc = df['z']

counter = 0
length = 0

for i in range(len(x)):
    if z_acc[i] > 0:
        length += 1
    else:
        if length > 25:
            counter += 1
        
        length = 0

print("Number of eye drop bottle used")
print(counter)