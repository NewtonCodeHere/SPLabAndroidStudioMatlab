clc;
clear;
close all;

% Input signal
x = [zeros(1,100) ones(1,150) -ones(1,100) ...
     ones(1,150) -ones(1,100)];

% Moving average window lengths
L1 = 3;
L2 = 10;
L3 = 51;

% FIR coefficients
b1 = ones(1,L1)/L1;
b2 = ones(1,L2)/L2;
b3 = ones(1,L3)/L3;

% Denominator coefficient
a = 1;

% Apply moving average filters
y1 = filter(b1,a,x);
y2 = filter(b2,a,x);
y3 = filter(b3,a,x);

% Plot
figure;

subplot(4,1,1);
plot(x,'r');
grid on;
title('Input Signal x(t)');
xlabel('Sample Number');
ylabel('Amplitude');

subplot(4,1,2);
plot(y1,'r');
grid on;
title('Filtered Output L = 3');
xlabel('Sample Number');
ylabel('Amplitude');

subplot(4,1,3);
plot(y2,'r');
grid on;
title('Filtered Output L = 10');
xlabel('Sample Number');
ylabel('Amplitude');

subplot(4,1,4);
plot(y3,'r');
grid on;
title('Filtered Output L = 51');
xlabel('Sample Number');
ylabel('Amplitude');

% Delay line for L = 51
hold on;
xline(300,'b','LineWidth',1.5);
hold off;
