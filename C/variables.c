/*
	Muhammed Kadir Yücel
	07.08.2017
	
	http://www.mkytr.com
*/

#include <stdarg.h> /* va_list için */
#include <stdio.h> /* printf ve puts için */
#include <stdlib.h> /* atoi için */

int topla(int num, ...)
{ /* `num` parametresi kaç değer gönderdiğimizi belirtir */
	va_list degerler;
	va_start(degerler, num);
	
	int sonuc = 0;
	
	for(int i = 0; i < num; i++)
	{
		sonuc += va_arg(degerler, int); /* her değer için değer tipi belirtmek zorundayız */
	}
	
	return sonuc;
}

int main()
{
	
	int toplam = topla(4, 10, 20, 40, 30);
	printf("Sonuc: %d\n", toplam);
	
	return 0;
}