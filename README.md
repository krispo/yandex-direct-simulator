# Yandex-direct sandbox web service with dummy functionality

This application would allow to simulate a real context advertising process in Yandex search network.
The yandex.direct sandbox is useful for testing specific functions in your applications, when using yandex.direct api. 
But we can't use this feature for simulating the real process. That's why we need something else.

This service would be used mainly for testing the whole bid system: Web Optimization Service (WOS) and Web Client for WOS. 
Particularly, it is designed to improve the main Optimization algorithm in WOS.

## Description

Suppose if particular banner has shown using corresponding banner-phrase `i` on a web search, there is specific prior probability `p_i` to be clicked on this banner on the certain place on web search.
In this case only for one show the "click" event for banner-phrase `i` has `Bernoulli(p_i)` distribution. But in the case of multiple shows (`n_i`) the "clicks" event has `Binomial(n_i,p_i)` distribution.

* We suppose prior probability is fixed for any banner-phrase on the certain place (premium Max, premium Min, Max, Min), but can be different from one to another. We define this at first.
* We can affect on the number of shows by changing bids