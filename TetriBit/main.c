#include <stdio.h>
#include <math.h>

void harta(unsigned long long h)
{
	int i=0, n;
	for(n = 63; n >= 0; --n){
		if (i % 8 == 0)
		{
			printf("\n");
		}
		if ((h&(1ul<<n))==0) {
			printf(".");
			h = (h - (1ul << n));
		}
		else {
			printf("#");
		}
		++i;	
	}
	printf("\n");
	return;

}

float score(unsigned long long h, int cmplines) {
	int n=0, zeros=0;
	float sc;
	for (n = 63; n>=0; --n){
		if ((h&(1ul<<n))==0) {
			++zeros;
		}
	}
	sc = sqrt(zeros) + pow(1.25, cmplines);

	return sc;

}

unsigned long long mutare(unsigned long long map){
	unsigned long long fig;
	int i=0, k=0; int miscari[7];
	scanf("%llu", &fig);
	
	for(i=0; i<8; ++i) {
		scanf("%d", &miscari[i]);
	}

	harta(map);
	if ((map | (fig<<56)) == (map + (fig<<56))) {
		map = (map | fig<<56);
		harta(map);
		printf("%llu", fig<<(8*6));
	}

	for(k=6; k>=0; k--) {
		printf("\n%llu %llu", map, fig<<(8*k));
		if((map | (fig<<(8*k))) == (map - (fig<<(8*(k+1))) + (fig<<(8*k)))) {
			harta(map);
			map = (map - (fig<<(8*(k+1))));
			harta(map);
			map = (map | (fig<<(8*k)));
			harta(map);
		}
		else {
			break;
		}
	}
	
	printf("\n%llu", map);
	return map;

}

int coliziune()
{
	return 0;
}

int clearlines() {
	return 0;
}

int main()
{
	unsigned long long map = 0;
	int nrmutari, p, lines = 0;
	scanf("%llu", &map);
	scanf("%d", &nrmutari);
	harta(map);
	
	for (p = nrmutari; p>0; --p) {
		map = mutare(map);
	}
	
	harta(map);
	printf("\nGAME OVER!\n");
	printf("\nScore:%.2f", score(map, lines));
	
	return 0;
}
