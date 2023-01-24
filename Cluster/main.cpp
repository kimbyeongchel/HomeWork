#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#pragma warning(disable : 4996)
#define _CRT_SECURE_NO_WARNING

// 함수 처리와 calloc을 malloc 으로 수정, 중심정 배열 복사 해결하기
// 추가 실험

typedef struct point {
    double x;
    double y;
} Point;

typedef struct next {
    double xsum;
    double ysum;
    double lon;
    int count;
} Next;

double length(Point* a, Point* b) {
    return sqrt((a->x - b->x) * (a->x - b->x) + (a->y - b->y) * (a->y - b->y));
}

int main() {
    char name[80];
    Point* coor;
    Point* center;
    Next* tc;
    int k;
    int number, repeat = 1, same = 0;
    int pos = 0, po1 = 0, po2 = 0;
    double min = 0, max = 0;
    bool one = true;

    printf("파일 이름과 k값을 입력하세요 : ");
    scanf("%s %d", name, &k);

    FILE* fp = fopen(name, "r");
    if (fp == NULL) {
        puts("파일 오픈 실패");
        return -1;
    }

    fscanf(fp, "%d", &number);

    coor = (Point*)calloc((number + 1), sizeof(Point));
    center = (Point*)calloc((k + 1), sizeof(Point));
    tc = (Next*)calloc((k + 1), sizeof(Next));

    for (int j = 0; j < number; j++)
        fscanf(fp, "%lf %lf", &coor[j].x, &coor[j].y);

    fclose(fp);

    center[0] = coor[0];
    po1++;
    max = length(&coor[0], &center[0]);
    for (int i = 0; i < number; i++) {
        if (max <= length(&coor[i], &center[0])) {
            max = length(&coor[i], &center[0]);
            pos = i;
        }
    }

    center[1] = coor[pos];
    po1++;

    while (po1 < k) {
        max = 0;
        for (int j = 0; j < number; j++) {
            one = true;
            for (int i = 0; i < po1; i++) {

                if (one) {
                    min = length(&coor[j], &center[i]);
                    one = false;
                }

                if (min >= length(&coor[j], &center[i])) {
                    min = length(&coor[j], &center[i]);
                    pos = j;
                }
            }
            if (max <= min) {
                max = min;
                po2 = pos;
            }
        }
        center[po1] = coor[po2];
        po1++;
    }
    printf("초기 클러스터의 구성:\n");
    for (int i = 0; i < k; i++)
        printf("	클러스터 %d: 중심점 = (%lf, %lf)\n", i, center[i].x, center[i].y);
    while (1) {
        for (int i = 0; i < number; i++) {
            one = true;
            max = 0;
            for (int j = 0; j < k; j++) {
                if (one) {
                    min = length(&coor[i], &center[j]);
                    one = false;
                }

                if (min >= length(&coor[i], &center[j])) {
                    min = length(&coor[i], &center[j]);
                    pos = i;
                    po2 = j;
                }
            }

            (tc[po2].count)++;
            tc[po2].xsum += coor[pos].x;
            tc[po2].ysum += coor[pos].y;
        }

        same = 0;
        for (int i = 0; i < k; i++) {
            if (tc[i].xsum / tc[i].count == center[i].x && tc[i].ysum / tc[i].count == center[i].y)
                same++;
        }
        if (same == k)
            break;

        for (int i = 0; i < k; i++) {
            center[i].x = (tc[i].xsum) / (tc[i].count);
            tc[i].xsum = 0;
            center[i].y = (tc[i].ysum) / (tc[i].count);
            tc[i].ysum = 0;
            tc[i].count = 0;
        }

        repeat++;
        printf("%d번째 클러스터 구성:\n", repeat);
        for (int i = 0; i < k; i++) {
            printf("	클러스터 %d: 중심점 = (%lf, %lf)\n", i, center[i].x, center[i].y);
        }
    }

    for (int i = 0; i < number; i++) {
        one = true;
        max = 0;
        for (int j = 0; j < k; j++) {
            if (one) {
                min = length(&coor[i], &center[j]);
                one = false;
            }

            if (min >= length(&coor[i], &center[j])) {
                min = length(&coor[i], &center[j]);
                pos = i;
                po2 = j;
            }
        }
        if (tc[po2].lon <= length(&coor[pos], &center[po2])) {
            tc[po2].lon = length(&coor[pos], &center[po2]);
        }
    }
    printf("###클러스터 구성완료!! : 반복 횟수 = %d\n", repeat);
    for (int i = 0; i < k; i++) {
        printf("	클러스터 %d: 중심점 = (%lf, %lf), point 수 = %d, 최장거리 = %lf\n", i, tc[i].xsum / tc[i].count, tc[i].ysum / tc[i].count, tc[i].count, tc[i].lon);
    }

    free(coor);
    free(center);
    free(tc);
}