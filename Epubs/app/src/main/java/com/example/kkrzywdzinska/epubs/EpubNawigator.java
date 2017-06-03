
package com.example.kkrzywdzinska.epubs;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;



public class EpubNawigator {

    private int nBooks;
    private EpubManipulator[] books;
    private Panel[] views;
    private MainActivity activity;
    private static Context context;

    public EpubNawigator(int numberOfBooks, MainActivity a) {
        nBooks = numberOfBooks;
        books = new EpubManipulator[nBooks];
        views = new Panel[nBooks];
        activity = a;
       // Log.d("Aktywnosc", data.getStringExtra());
        context = a.getBaseContext();
    }

    public boolean openBook(String path, int index) {
        try {
            if (books[index] != null)
                books[index].destroy();

            books[index] = new EpubManipulator(path, index + "", context);
            changePanel(new WidokKsiazka(), index);
            setBookPage(books[index].getSpineElementPath(0), index);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setBookPage(String page, int index) {

        if (books[index] != null) {
            books[index].goToPage(page);
        }
        loadPageIntoView(page, index);
    }

    public void loadPageIntoView(String pathOfPage, int index) {
        ZobaczStatusEnum state = ZobaczStatusEnum.notes;

        if (books[index] != null)
            if ((pathOfPage.equals(books[index].getCurrentPageURL()))
                    || (books[index].getPageIndex(pathOfPage) >= 0))
                state = ZobaczStatusEnum.books;

        if (books[index] == null)
            state = ZobaczStatusEnum.notes;

        if (views[index] == null || !(views[index] instanceof WidokKsiazka))
            changePanel(new WidokKsiazka(), index);

        ((WidokKsiazka) views[index]).status = state;
        ((WidokKsiazka) views[index]).loadPage(pathOfPage);
    }


    public void goToNextChapter(int book) throws Exception {
        setBookPage(books[book].goToNextChapter(), book);
    }

    public void goToPrevChapter(int book) throws Exception {
        setBookPage(books[book].goToPreviousChapter(), book);
    }

    public void closeView(int index) {
        // dostepne inne panele
        if (books[index] != null
                && (!(views[index] instanceof WidokKsiazka) || (((WidokKsiazka) views[index]).status != ZobaczStatusEnum.books))) {
            WidokKsiazka v = new WidokKsiazka();
            changePanel(v, index);
            v.loadPage(books[index].getCurrentPageURL());
        } else // pozostale
        {
            if (books[index] != null)
                try {
                    books[index].destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            activity.removePanel(views[index]);

            while (index < nBooks - 1) {
                books[index] = books[index + 1];
                if (books[index] != null)
                    books[index].changeDirName(index + "");


                views[index] = views[index + 1];
                if (views[index] != null) {
                    views[index].setKey(index);
                    if (views[index] instanceof WidokKsiazka
                            && ((WidokKsiazka) views[index]).status == ZobaczStatusEnum.books)
                        ((WidokKsiazka) views[index]).loadPage(books[index]
                                .getCurrentPageURL());
                }
                index++;
            }
            books[nBooks - 1] = null;
            views[nBooks - 1] = null;
        }
    }


    public boolean displayMetadata(int book) {
        boolean res = true;

        if (books[book] != null) {
            WidokDane dv = new WidokDane();
            dv.loadData(books[book].metadata());
            changePanel(dv, book);
        } else
            res = false;

        return res;
    }

    public boolean displayTOC(int book) {
        boolean res = true;

        if (books[book] != null)
            setBookPage(books[book].tableOfContents(), book);
        else
            res = false;
        return res;
    }

    public void changeCSS(int book, String[] settings) {
        books[book].addCSS(settings);
        loadPageIntoView(books[book].getCurrentPageURL(), book);
    }


    public boolean exactlyOneBookOpen() {
        int i = 0;

        while (i < nBooks && books[i] == null)
            i++;

        if (i == nBooks)
            return false;

        i++;

        while (i < nBooks && books[i] == null)
            i++;

        if (i == nBooks)
            return true;
        else

            return false;
    }


    public void changePanel(Panel p, int index) {
        if (views[index] != null) {
            activity.removePanelWithoutClosing(views[index]);
            p.changeWeight(views[index].getWeight());
        }

        if (p.isAdded())
            activity.removePanelWithoutClosing(p);

        views[index] = p;
        activity.addPanel(p);
        p.setKey(index);

        for (int i = index + 1; i < views.length; i++)
            if (views[i] != null) {
                activity.detachPanel(views[i]);
                activity.attachPanel(views[i]);
            }
    }

    private Panel newPanelByClassName(String className) {
        if (className.equals(WidokKsiazka.class.getName()))
            return new WidokKsiazka();
        if (className.equals(WidokDane.class.getName()))
            return new WidokDane();
        return null;
    }

    public void saveState(SharedPreferences.Editor editor) {


        for (int i = 0; i < nBooks; i++)
            if (books[i] != null) {
                editor.putInt(getS(R.string.CurrentPageBook) + i,
                        books[i].getCurrentSpineElementIndex());
                editor.putInt(getS(R.string.LanguageBook) + i,
                        books[i].getCurrentLanguage());
                editor.putString(getS(R.string.nameEpub) + i,
                        books[i].getDecompressedFolder());
                editor.putString(getS(R.string.pathBook) + i,
                        books[i].getFileName());
                try {
                    books[i].closeStream();
                } catch (IOException e) {
                    Log.e(getS(R.string.error_CannotCloseStream),
                            getS(R.string.Book_Stream) + (i + 1));
                    e.printStackTrace();
                }
            } else {
                editor.putInt(getS(R.string.CurrentPageBook) + i, 0);
                editor.putInt(getS(R.string.LanguageBook) + i, 0);
                editor.putString(getS(R.string.nameEpub) + i, null);
                editor.putString(getS(R.string.pathBook) + i, null);
            }


        for (int i = 0; i < nBooks; i++)
            if (views[i] != null) {
                editor.putString(getS(R.string.ViewType) + i, views[i]
                        .getClass().getName());
                views[i].saveState(editor);
                activity.removePanelWithoutClosing(views[i]);
            } else
                editor.putString(getS(R.string.ViewType) + i, "");
    }

    public boolean loadState(SharedPreferences preferences) {
        boolean ok = true;

        int current, lang;
        String name, path;
        for (int i = 0; i < nBooks; i++) {
            current = preferences.getInt(getS(R.string.CurrentPageBook) + i, 0);
            lang = preferences.getInt(getS(R.string.LanguageBook) + i, 0);
            name = preferences.getString(getS(R.string.nameEpub) + i, null);
            path = preferences.getString(getS(R.string.pathBook) + i, null);

            if (path != null) {
                try {
                    books[i] = new EpubManipulator(path, name, current, lang,
                            context);
                    books[i].goToPage(current);
                } catch (Exception e1) {


                    try {
                        books[i] = new EpubManipulator(path, i + "", context);
                        books[i].goToPage(current);
                    } catch (Exception e2) {
                        ok = false;
                    } catch (Error e3) {
                        ok = false;
                    }
                } catch (Error e) {

                    try {
                        books[i] = new EpubManipulator(path, i + "", context);
                        books[i].goToPage(current);
                    } catch (Exception e2) {
                        ok = false;
                    } catch (Error e3) {
                        ok = false;
                    }
                }
            } else
                books[i] = null;
        }

        return ok;
    }

    public void loadViews(SharedPreferences preferences) {
        for (int i = 0; i < nBooks; i++) {
            views[i] = newPanelByClassName(preferences.getString(
                    getS(R.string.ViewType) + i, ""));
            if (views[i] != null) {
                activity.addPanel(views[i]);
                views[i].setKey(i);
                views[i].loadState(preferences);
            }
        }
    }

    public String getS(int id) {
        return context.getResources().getString(id);
    }
}
