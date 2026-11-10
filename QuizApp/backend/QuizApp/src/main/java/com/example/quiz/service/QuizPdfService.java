package com.example.quiz.service;

import com.example.quiz.section.Question;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class QuizPdfService {

    private static final Font TITLE = new Font(Font.HELVETICA, 18, Font.BOLD);
    private static final Font Q_FONT = new Font(Font.HELVETICA, 12, Font.BOLD);
    private static final Font OPT_FONT = new Font(Font.HELVETICA, 11);
    private static final Font META = new Font(Font.HELVETICA, 10, Font.ITALIC);
    private static final Font ANS_FONT = new Font(Font.HELVETICA, 11, Font.BOLD);

    /** includeAnswers=false gives a blank question paper; true adds an answer key page. */
    public byte[] generate(String title, List<Question> questions, boolean includeAnswers) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Paragraph heading = new Paragraph(title, TITLE);
            heading.setSpacingAfter(14);
            doc.add(heading);

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);

                doc.add(new Paragraph((i + 1) + ". " + q.question(), Q_FONT));
                doc.add(new Paragraph("Section " + (q.sectionIndex() + 1), META));

                List<String> opts = q.options();
                for (int k = 0; k < opts.size(); k++) {
                    char label = (char) ('A' + k);
                    Paragraph p = new Paragraph(label + ") " + opts.get(k), OPT_FONT);
                    p.setIndentationLeft(20);
                    if (k == opts.size() - 1) {
                        p.setSpacingAfter(12);   // gap between questions
                    }
                    doc.add(p);
                }
            }

            if (includeAnswers) {
                doc.newPage();

                Paragraph keyHeading = new Paragraph("Answer Key", TITLE);
                keyHeading.setSpacingAfter(14);
                doc.add(keyHeading);

                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);
                    int ci = q.correctIndex();
                    char correct = (char) ('A' + ci);

                    Paragraph a = new Paragraph(
                            (i + 1) + ". " + correct + ") " + q.options().get(ci), ANS_FONT);
                    boolean hasExplanation = q.explanation() != null && !q.explanation().isBlank();
                    if (!hasExplanation) {
                        a.setSpacingAfter(8);
                    }
                    doc.add(a);

                    if (hasExplanation) {
                        Paragraph e = new Paragraph("Explanation: " + q.explanation(), META);
                        e.setIndentationLeft(20);
                        e.setSpacingAfter(8);
                        doc.add(e);
                    }
                }
            }

            doc.close();
            return removeBlankPages(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    /** Safety net: drops any page that contains no text. */
    private byte[] removeBlankPages(byte[] pdf) throws IOException {
        try (PDDocument pd = Loader.loadPDF(pdf)) {
            PDFTextStripper stripper = new PDFTextStripper();
            for (int page = pd.getNumberOfPages(); page >= 1; page--) {
                if (pd.getNumberOfPages() == 1) break;
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                if (stripper.getText(pd).isBlank()) {
                    pd.removePage(page - 1);
                }
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            pd.save(result);
            return result.toByteArray();
        }
    }
}