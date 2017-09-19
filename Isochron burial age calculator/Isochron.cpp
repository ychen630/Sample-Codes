/****************
Yixin Chen
August 2015

This program calculate Isochron CN burial ages based on York fit
Programming language C++

This program reads an input file named sample.txt,
and outputs the results on the command line

*****************/

#include <iostream>
#include <string>
#include <math.h>

using namespace std;

//constant
const unsigned MAX_ELS = 10;
const double tau_be = 1.387*1e6 / log(2);
const double tau_al = 0.717*1e6 / log(2);
const double tau_burial = tau_be*tau_al / (tau_be - tau_al);
const double tol = 1e-15;

const unsigned N_iter_max = 3000;

//struct to store isotop parameters
struct Isotop{
	double al[MAX_ELS];
	double be[MAX_ELS];
	double sigma_al[MAX_ELS];
	double sigma_be[MAX_ELS];
	double r[MAX_ELS];
};

double square(double a); //calculate square of a number
double sum(double a[], unsigned elements); 
double sum(double a[], double b[], unsigned elements);
double sum(double a[], double b[], double c[], unsigned elements);
void show(Isotop s, unsigned elements);
void show(double a[], unsigned elements); //print results
void york_fit(Isotop & s, double & a, double & b, double & sigma_b, unsigned n); //york fit

int main(){

	double a, b, sigma_b;
	Isotop intial;
	unsigned n;
	
	//open file, read cosmogenic data
	ifstream infile("sample.txt");
	if(infile.fail()){
		cout << "Error opening file: no file named sample.txt in the current folder." << endl;
		exit(EXIT_FAILURE);
	}
	
	int v1, v2, v3, v4, v5, vi = 0;
	while (infile >> v1 >> v2 >> v3 >> v4 >> v5){
		intial.al[vi] = v1;
		intial.be[vi] = v2;
		intial.sigma_al[vi] = v3;
		intial.sigma_be[vi] = v4;
		intial.r[vi] = v5;
		vi++;
	}
	infile.close();

/*
	n = 8;
	intial = { { 0.0008, 0.00084, 0.001, 0.00085, 0.0027, 0.00071, 0.00043, 0.0016 }, { 0.037, 0.035, 0.032, 0.04, 0.013, 0.038, 0.042, 0.03 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
	for (unsigned i = 0; i < n; i++){
	intial.sigma_al[i] = intial.al[i] * 0.1;
	intial.sigma_be[i] = intial.be[i] * 0.03;
	} //	test data set
*/  



/* xxbm glaciation data set n=5
	n = 5;
	intial = { { 10577410.8, 28068977.71, 115754146.7, 17708590.21, 6041688.017 }, { 1804868.52, 4808612.25, 19993202.7, 3120932.85, 1435019.7 }, { 438598.0771, 1041457.69, 5147763.543, 778676.054, 2161647.712 }, { 26143.6, 102310.74, 364979.81, 43836.13, 40242.08 }, { 0, 0, 0, 0, 0 } };
*/

/* gongba data set n=3
	n = 3;
	intial = { { 697822.0399, 1744212.298, 6173563.167 }, { 259373.38, 432457.07, 1247902.72 }, { 224471.89, 121237.48, 358192.16 }, { 10807.79, 10832.23, 34612.45 }, { 0, 0, 0, 0, 0 } };
*/
	
	/*
	for (unsigned i = 0; i < n; i++){
		intial.al[i] = intial.al[i] / 1e6;
		intial.be[i] = intial.be[i] / 1e6;
		intial.sigma_al[i] = intial.sigma_al[i] / 1e6;
		intial.sigma_be[i] = intial.sigma_be[i] / 1e6;

	} // change to 1e6
	*/

	double t, sigma_t;
	double c_al, c_be;
	double r_0 = 6.75;
	double be_r[MAX_ELS];
	Isotop temp=intial;

	//calculate and output ages
	for (unsigned i = 0; i<10; i++){
		york_fit(temp, a, b, sigma_b, n);
		
		t = tau_burial*log(r_0 / b);
		sigma_t = sigma_b / b;
	
		cout << "b: "<< b << ";    a:" << a << endl;
		cout << "time: " << t / 1e6 << "¡À" << sigma_t / 1e6 << " Ma" << endl;

		double r_c = r_0*tau_al*(1 - exp(-t / tau_al)) / (tau_be*(1 - exp(-t / tau_be)));
		c_al = a*r_c / (r_c - r_0*exp(-t / tau_burial));
		c_be = a / (r_c - r_0*exp(-t / tau_burial));

		cout << "c_al: " << c_al << "; c_be: " << c_be << endl;
		cout << "r_c: " << r_c << "; r_0: " << r_0 << endl;

		
		for (unsigned j = 0; j<n; j++){
			be_r[j] = ((intial.al[j] - c_al) / (intial.be[j] - c_be)) / exp(-t / tau_burial)/r_0;
			cout << "be-r[ " << j << "]: " << be_r[j] << endl;
			temp.be[j] = temp.be[j] * be_r[j];
		}
	}

	show(intial, n);

	cout << b << "   " << sigma_b << endl;
	cout << a << "   " << endl;
	cout << t << "   " << sigma_t << endl;


	return 0;
}


double square(double a){
	return a*a;
}

double sum(double a[], unsigned elements){
	double sum = 0;
	for (unsigned i = 0; i < elements; i++)
		sum += a[i];
	return sum;
}


double sum(double a[], double b[], unsigned elements){
	double sum = 0;
	for (unsigned i = 0; i < elements; i++)
		sum += (a[i] * b[i]);
	return sum;
}

double sum(double a[], double b[], double c[], unsigned elements){
	double sum = 0;
	for (unsigned i = 0; i < elements; i++)
		sum += (a[i] * b[i] * c[i]);
	return sum;
}

void show(Isotop s, unsigned elements){
	show(s.be, elements);
	show(s.sigma_be, elements);
	show(s.al, elements);
	show(s.sigma_al, elements);
}

void show(double a[], unsigned elements){
	for (unsigned i = 0; i<elements; i++)
		cout << a[i] << ";     ";
	cout << endl;
}

void york_fit(Isotop & s, double & a, double & b, double & sigma_b, unsigned n){

	double al_ave, be_ave;
	al_ave = sum(s.al, n) / n; be_ave = sum(s.be, n) / n;

	double suma = 0, sumb = 0;
	for (unsigned i = 0; i<n; i++){
		suma += ((s.al[i] - al_ave)*(s.be[i] - be_ave));
		sumb += square(s.be[i] - be_ave);
	}
	b = suma / sumb;
	a = al_ave - b*be_ave;

	double b_save[N_iter_max];
	b_save[0] = b;
	double be_bar, al_bar, u[MAX_ELS], v[MAX_ELS], w[MAX_ELS];
	double beta[MAX_ELS];
	for (unsigned j = 0; j < N_iter_max; j++){
		for (unsigned i = 0; i < n; i++)
			w[i] = 1 / (square(s.sigma_al[i]) + square(b)*square(s.sigma_be[i]) - 2 * b*s.r[i] * s.sigma_al[i] * s.sigma_be[i]);

		be_bar = sum(w, s.be, n) / sum(w, n);
		al_bar = sum(w, s.al, n) / sum(w, n);

		for (unsigned i = 0; i < n; i++){
			u[i] = s.be[i] - be_bar;
			v[i] = s.al[i] - al_bar;
		}

		
		for (unsigned i = 0; i < n; i++)
			beta[i] = w[i] * (u[i] * square(s.sigma_al[i]) + b*v[i] * square(s.sigma_be[i]) - (b*u[i] + v[i])*s.r[i] * s.sigma_al[i] * s.sigma_be[i]);

		b = sum(w, beta, v, n) / sum(w, beta, u, n);
		b_save[j + 1] = b;
		if (abs((b_save[j + 1] - b_save[j]) / b_save[j + 1]) < tol) { cout << "regression: " << j << endl; break; }
	}
	a = al_bar - b*be_bar;

	Isotop final;
	for (unsigned i = 0; i < n; i++){
		final.be[i] = be_bar + beta[i];
		final.al[i] = al_bar + b*beta[i];
	}

	be_bar = sum(w, final.be, n) / sum(w, n);
	al_bar = sum(w, final.al, n) / sum(w, n);

	for (unsigned i = 0; i < n; i++){
		u[i] = final.be[i] - be_bar;
		v[i] = final.al[i] - al_bar;
	}

	sigma_b = sqrt(1 / sum(w, u, u, n));
	//	sigma_a=sqrt(1/sum(w, n)+square(be_bar)*square(al_bar));
}