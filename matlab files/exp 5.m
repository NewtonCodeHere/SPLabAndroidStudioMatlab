clc;
clear;
close all;

fs=8000;
f=[0 600 1000 1400 2000 4000];
a=[1.25 2 1 0.25 0.1 0];
n=31;

h=fir2(n,f/(fs/2),a);

N=256;
t=(0:N-1)/fs;
x=chirp(t,0,N/fs,fs/2);

figure;
plot(t,x);
title('Input Chirp');
xlabel('Time (s)');
ylabel('Amplitude');
grid on;

y=filter(h,1,x);
Y=fft(y);

ib=2;
bits=0;
err=0;
qh=[];

for b=4:16
    fb=b-ib-1;
    q=sfi(h,b,fb);
    c=q.data;
    yq=filter(c,1,x);
    Yq=fft(yq);
    e=max(abs(Yq-Y))/max(abs(Y));
    if e<0.05
        bits=b;
        err=e;
        qh=c;
        break;
    end
end

if bits==0
    disp('No bit count satisfies the error limit');
    return;
end

yq=filter(qh,1,x);
Yq=fft(yq);
freq=(0:N/2)*fs/N;

figure;
subplot(2,1,1);
plot(freq,abs(Y(1:N/2+1)));
title('Original FIR Spectrum');
xlabel('Frequency (Hz)');
ylabel('Magnitude');
grid on;

subplot(2,1,2);
plot(freq,abs(Yq(1:N/2+1)));
title('Quantized FIR Spectrum');
xlabel('Frequency (Hz)');
ylabel('Magnitude');
grid on;

[H,F]=freqz(h,1,1024,fs);
[Hq,Fq]=freqz(qh,1,1024,fs);
[p,k]=max(abs(H));

figure;
plot(F,abs(H));
hold on;
plot(Fq,abs(Hq),'--');
plot(f,a,'o');
title('FIR Frequency Response');
xlabel('Frequency (Hz)');
ylabel('Magnitude');
legend('Original','Quantized','Desired');
grid on;

fprintf('Minimum bits = %d\n',bits);
fprintf('Fractional bits = %d\n',bits-ib-1);
fprintf('Maximum relative error = %.6f\n',err);
fprintf('Peak magnitude = %.4f\n',p);
fprintf('Peak frequency = %.2f Hz\n',F(k));
