clc;
clear;
close all;

f=[0 0.2 0.4 0.41 1];
m=[0 1 1 0 0];
n=8;

[b,a]=yulewalk(n,f,m);
sos=tf2sos(b,a);

[H,w]=freqz(b,a,1024);
figure;
plot(w/pi,abs(H));
title('IIR Bandpass Filter');
xlabel('Normalized Frequency');
ylabel('Magnitude');
grid on;

fs=8000;
t=0:1/fs:1-1/fs;
x=chirp(t,0,t(end),fs/2);

y=filter(b,a,x);
z=x;
for i=1:size(sos,1)
    z=filter(sos(i,1:3),sos(i,4:6),z);
end

ib=8;
N=length(x);
Y=fft(y);
bits=[8 12 16 20];
err1=zeros(size(bits));
err2=zeros(size(bits));

for k=1:length(bits)
    tb=bits(k);
    fb=tb-ib-1;

    bq=sfi(b,tb,fb).data;
    aq=sfi(a,tb,fb).data;
    yq=filter(bq,aq,x);
    Yq=fft(yq);
    err1(k)=max(abs(Yq-Y))/max(abs(Y));

    sq=sfi(sos,tb,fb).data;
    zq=x;
    for i=1:size(sq,1)
        zq=filter(sq(i,1:3),sq(i,4:6),zq);
    end
    Zq=fft(zq);
    Z=fft(z);
    err2(k)=max(abs(Zq-Z))/max(abs(Z));
end

figure;
plot(bits,err1,'bo-');
hold on;
plot(bits,err2,'rs-');
xlabel('Total Bits');
ylabel('Relative Error');
title('Coefficient Quantization Error');
legend('Direct Form','SOS Cascade');
grid on;

tb=20;
fb=tb-ib-1;
bq=sfi(b,tb,fb).data;
aq=sfi(a,tb,fb).data;
yq=filter(bq,aq,x);

sq=sfi(sos,tb,fb).data;
zq=x;
for i=1:size(sq,1)
    zq=filter(sq(i,1:3),sq(i,4:6),zq);
end

freq=(0:N/2-1)*fs/N;
Y1=fft(y);
Y2=fft(yq);
Y3=fft(zq);

figure;
subplot(3,1,1);
plot(freq,abs(Y1(1:N/2)));
title('Direct Form - Unquantized');
ylabel('Magnitude');
grid on;

subplot(3,1,2);
plot(freq,abs(Y2(1:N/2)));
title('Direct Form - Quantized');
ylabel('Magnitude');
grid on;

subplot(3,1,3);
plot(freq,abs(Y3(1:N/2)));
title('SOS Cascade - Quantized');
xlabel('Frequency (Hz)');
ylabel('Magnitude');
grid on;
