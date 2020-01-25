package org.mikuclub.app.javaBeans.resources.base;


import java.io.Serializable;
import java.util.Date;

public class PrivateMessage implements Serializable
{

        private int id;
        private int sender_id;
        private int recipient_id;
        private String content;
        private int respond;
        private Date date;
        private Author author;


        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public int getSender_id()
        {
                return sender_id;
        }

        public void setSender_id(int sender_id)
        {
                this.sender_id = sender_id;
        }

        public int getRecipient_id()
        {
                return recipient_id;
        }

        public void setRecipient_id(int recipient_id)
        {
                this.recipient_id = recipient_id;
        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
        {
                this.content = content;
        }

        public int getRespond()
        {
                return respond;
        }

        public void setRespond(int respond)
        {
                this.respond = respond;
        }

        public Date getDate()
        {
                return date;
        }

        public void setDate(Date date)
        {
                this.date = date;
        }

        public Author getAuthor()
        {
                return author;
        }

        public void setAuthor(Author author)
        {
                this.author = author;
        }
}
