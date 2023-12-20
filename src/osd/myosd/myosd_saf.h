// license:BSD-3-Clause
//============================================================
//
//  myosd_saf.h - SAF header file
//
//  MAME4DROID by David Valdeita (Seleuco)
//
//============================================================

#ifndef __MYOSD_SAF_H__
#define __MYOSD_SAF_H__

extern int myosd_droid_using_saf;
extern int myosd_droid_reload;
extern int myosd_droid_savestatesinrompath;
extern std::string myosd_droid_safpath;

int myosd_safOpenFile(const char *pathName,const char *mode);
int *myosd_safReadDir(const char *dirName, int reload);
std::string *myosd_safGetNextDirEntry(int *id);
void myosd_safCloseDir(int *id);


#endif
