package zw.ac.msu.dzimbahub.payment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="rental_payments", uniqueConstraints=@UniqueConstraint(name="uk_payment_reference", columnNames="merchant_reference"))
public class PaymentRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="merchant_reference",nullable=false,unique=true,length=80) private String merchantReference;
 @Column(nullable=false) private Long propertyId;
 @Column(nullable=false) private Long tenantId;
 @Column(nullable=false) private Long landlordId;
 @Column(nullable=false,length=180) private String tenantEmail;
 @Column(nullable=false,length=180) private String landlordEmail;
 @Column(nullable=false,length=200) private String propertyTitle;
 @Column(nullable=false) private double amount;
 @Column(nullable=false,length=30) private String method;
 @Column(nullable=false,length=30) private String status="PENDING";
 @Column(length=1000) private String pollUrl;
 @Column(length=1000) private String checkoutUrl;
 @Column(length=150) private String externalReference;
 @Column(length=1500) private String instructions;
 @Column(length=100) private String paynowReference;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 private Instant paidAt;
 protected PaymentRecord(){}
 public PaymentRecord(String ref,Long propertyId,Long tenantId,Long landlordId,String tenantEmail,String landlordEmail,String title,double amount,String method){this.merchantReference=ref;this.propertyId=propertyId;this.tenantId=tenantId;this.landlordId=landlordId;this.tenantEmail=tenantEmail;this.landlordEmail=landlordEmail;this.propertyTitle=title;this.amount=amount;this.method=method;this.status="PENDING";this.createdAt=Instant.now();}
 public Long getId(){return id;} public String getMerchantReference(){return merchantReference;} public Long getPropertyId(){return propertyId;} public Long getTenantId(){return tenantId;} public Long getLandlordId(){return landlordId;} public String getTenantEmail(){return tenantEmail;} public String getLandlordEmail(){return landlordEmail;} public String getPropertyTitle(){return propertyTitle;} public double getAmount(){return amount;} public String getMethod(){return method;} public String getStatus(){return status;} public String getPollUrl(){return pollUrl;} public String getCheckoutUrl(){return checkoutUrl;} public String getExternalReference(){return externalReference;} public String getInstructions(){return instructions;} public String getPaynowReference(){return paynowReference;} public Instant getCreatedAt(){return createdAt;} public Instant getPaidAt(){return paidAt;}
 public void setStatus(String v){status=v;} public void setPollUrl(String v){pollUrl=v;} public void setCheckoutUrl(String v){checkoutUrl=v;} public void setExternalReference(String v){externalReference=v;} public void setInstructions(String v){instructions=v;} public void setPaynowReference(String v){paynowReference=v;} public void setPaidAt(Instant v){paidAt=v;}
}
