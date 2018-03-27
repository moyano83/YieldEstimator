import pandas as pd
import numpy as np
from sklearn import datasets, linear_model
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score, median_absolute_error
import matplotlib.pyplot as plt
import sys

df = pd.read_csv(sys.argv[1])

x = df[['nr_pixels', 'occlusion']]

y = df['weight']



x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.3, random_state=42)

reg = linear_model.LinearRegression(copy_X=True, fit_intercept=True, n_jobs=1, normalize=False)
reg.fit(x_train, y_train)


weight = reg.predict(x_test)


# The coefficients
print('Coefficients: %s' % reg.coef_)
# Explained variance score: 1 is perfect prediction
print('Variance score: %.2f' % r2_score(weight, y_test))
# The mean squared error
print("Mean squared error: %.2f" % mean_squared_error(weight, y_test))
# The median absolute error
print('Median absolute error: %.2f' % median_absolute_error(weight, y_test))

fig, ax = plt.subplots()
ax.scatter(y_test, weight, edgecolors=(0, 0, 0))
ax.plot([y_test.min(), y_test.max()], [weight.min(), weight.max()], '--', lw=1)
ax.set_xlabel('Measured')
ax.set_ylabel('Predicted')
plt.show()