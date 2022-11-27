package com.info5059.casestudy.purchaseOrder;

import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;
import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
//import java.time.LocalDateTime;

//import org.springframework.cglib.beans.BeanCopier.Generator;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
//import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

public abstract class PurchaseOrderPDFGenerator extends AbstractPdfView {
        public static ByteArrayInputStream generatePurchaseOrder(String repid,
                        PurchaseOrderRepository purchaseOrderRepository,
                        VendorRepository vendorRepository,
                        ProductRepository productRepository) throws IOException, java.io.IOException {
                URL imageUrl = PurchaseOrderPDFGenerator.class.getResource("/static/images/vendor-logo.png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                // Initialize PDF document to be written to a stream not a file
                PdfDocument pdf = new PdfDocument(writer);
                // Document is the main object
                Document document = new Document(pdf);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                // add the image to the document
                // PageSize pg = PageSize.A4;
                Image img = new Image(ImageDataFactory.create(imageUrl)).scaleAbsolute(120, 100)
                                .setFixedPosition(70, 750);
                document.add(img);
                // now let's add a big heading
                document.add(new Paragraph("\n\n"));
                Locale locale = new Locale("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
                BigDecimal total = new BigDecimal(0.0);
                BigDecimal ExtTotPrice = new BigDecimal(0.0);
                BigDecimal tax = new BigDecimal(0.13);
                BigDecimal subTax = new BigDecimal(0.0);
                BigDecimal POtotal = new BigDecimal(0.0);
                try {
                        document.add(new Paragraph("\n"));
                        Optional<PurchaseOrder> optPurchaseOrder = purchaseOrderRepository
                                        .findById(Long.parseLong(repid));
                        if (optPurchaseOrder.isPresent()) {
                                PurchaseOrder purchaseOrder = optPurchaseOrder.get();
                                document.add(new Paragraph("Purchase Order# " + repid).setFont(font).setFontSize(18)
                                                .setBold()
                                                .setMarginRight(80)
                                                .setMarginTop(-20)
                                                .setTextAlignment(TextAlignment.RIGHT));
                                document.add(new Paragraph("\n\n"));

                                // Optional<Vendor> optVendor =
                                // vendorRepository.findById(purchaseOrder.getVendorid());
                                Optional<Vendor> optVendor = vendorRepository.findById(purchaseOrder.getVendorid());
                                if (optVendor.isPresent()) {
                                        Vendor vendor = optVendor.get();

                                        // 2 column table
                                        Table table = new Table(2);
                                        table.setBorder(Border.NO_BORDER);
                                        table.setWidth(new UnitValue(UnitValue.PERCENT, 30));
                                        // table headings
                                        Cell cell = new Cell().add(new Paragraph("Vendor:")
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        table.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getName())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        table.addCell(cell);
                                        // table details
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        table.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getAddress1())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        table.addCell(cell);

                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.RIGHT);
                                        table.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getCity())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.LEFT)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                                        table.addCell(cell);
                                        // table details
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.RIGHT);
                                        table.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getProvince())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        table.addCell(cell);

                                        // table details
                                        cell = new Cell().add(new Paragraph(" ")
                                                        .setFont(font)
                                                        .setFontSize(12))
                                                        .setBorder(Border.NO_BORDER)
                                                        .setTextAlignment(TextAlignment.RIGHT);
                                        table.addCell(cell);
                                        cell = new Cell().add(new Paragraph(vendor.getEmail())
                                                        .setFont(font)
                                                        .setFontSize(12)
                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER)
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setTextAlignment(TextAlignment.LEFT);
                                        table.addCell(cell);

                                        document.add(table);
                                } // end employee table
                                  // dump out the line items
                                document.add(new Paragraph("\n\n"));

                                // now a 5 column table
                                Table productTable = new Table(5);
                                productTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
                                // table headings
                                Cell cell = new Cell().add(new Paragraph("Product Code")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productTable.addCell(cell);

                                cell = new Cell().add(new Paragraph("Description")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold())
                                                .setTextAlignment(TextAlignment.CENTER);
                                productTable.addCell(cell);

                                cell = new Cell().add(new Paragraph("Qty Sold")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold()
                                                .setTextAlignment(TextAlignment.LEFT));
                                productTable.addCell(cell);

                                cell = new Cell().add(new Paragraph("Price")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold()
                                                .setTextAlignment(TextAlignment.LEFT));
                                productTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(" Ext. Price")
                                                .setFont(font)
                                                .setFontSize(12)
                                                .setBold()
                                                .setTextAlignment(TextAlignment.LEFT));
                                productTable.addCell(cell);
                                for (PurchaseOrderLineitem line : purchaseOrder.getItems()) {
                                        Optional<Product> optx = productRepository.findById(line.getProductid());
                                        if (optx.isPresent()) {
                                                Product product = optx.get();
                                                // second row
                                                cell = new Cell().add(new Paragraph(product.getId())
                                                                .setFont(font)
                                                                .setFontSize(12)
                                                                .setBold())
                                                                .setTextAlignment(TextAlignment.CENTER);
                                                productTable.addCell(cell);
                                                cell = new Cell().add(new Paragraph(product.getName())
                                                                .setFont(font)
                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.CENTER);
                                                productTable.addCell(cell);

                                                cell = new Cell().add(new Paragraph(Integer.toString(line.getQty()))
                                                                .setFont(font)
                                                                .setFontSize(12)
                                                                .setTextAlignment(TextAlignment.LEFT));
                                                productTable.addCell(cell);

                                                cell = new Cell().add(
                                                                new Paragraph(formatter.format(product.getCostprice()))
                                                                                .setFont(font)
                                                                                .setFontSize(12)
                                                                                .setTextAlignment(TextAlignment.LEFT));
                                                productTable.addCell(cell);

                                                ExtTotPrice = product.getCostprice().multiply(
                                                                BigDecimal.valueOf(line.getQty()),
                                                                new MathContext(8, RoundingMode.UP));

                                                cell = new Cell().add(
                                                                new Paragraph(formatter.format(ExtTotPrice))
                                                                                .setFont(font)
                                                                                .setFontSize(12))
                                                                .setTextAlignment(TextAlignment.LEFT);
                                                productTable.addCell(cell);

                                                total = total.add(ExtTotPrice, new MathContext(8, RoundingMode.UP));
                                                subTax = total.multiply(tax, new MathContext(8, RoundingMode.UP));
                                                POtotal = total.add(subTax, new MathContext(8, RoundingMode.UP));

                                        } // end if
                                } // end for
                                  // report total
                                cell = new Cell(1, 4).add(new Paragraph("Sub Total:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productTable.addCell(cell);

                                cell = new Cell().add(new Paragraph(formatter.format(total)))
                                                .setTextAlignment(TextAlignment.LEFT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productTable.addCell(cell);

                                cell = new Cell(1, 4).add(new Paragraph("Tax:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(formatter.format(subTax)))
                                                .setTextAlignment(TextAlignment.LEFT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productTable.addCell(cell);

                                cell = new Cell(1, 4).add(new Paragraph("PO Total:"))
                                                .setBorder(Border.NO_BORDER)
                                                .setTextAlignment(TextAlignment.RIGHT);
                                productTable.addCell(cell);
                                cell = new Cell().add(new Paragraph(formatter.format(POtotal)))
                                                .setTextAlignment(TextAlignment.LEFT)
                                                .setBackgroundColor(ColorConstants.YELLOW);
                                productTable.addCell(cell);

                                document.add(productTable);
                                document.add(new Paragraph("\n\n"));
                                DateTimeFormatter dateFormatter = DateTimeFormatter
                                                .ofPattern("yyyy-MM-dd h:mm a");
                                // document.add(new Paragraph(dateFormatter.format(LocalDateTime.now()))
                                document.add(new Paragraph(dateFormatter.format(purchaseOrder.getPodate()))
                                                .setTextAlignment(TextAlignment.CENTER));
                                document.close();

                        }
                } catch (Exception ex) {
                        Logger.getLogger(PurchaseOrderPDFGenerator.class.getName()).log(Level.SEVERE, "null", ex);
                }
                // finally send stream back to the controller
                return new ByteArrayInputStream(baos.toByteArray());
        }
}