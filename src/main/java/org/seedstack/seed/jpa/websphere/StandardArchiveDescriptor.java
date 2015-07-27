/*
 * Creation : 30 juin 2015
 */
package org.seedstack.seed.jpa.websphere;

import java.net.URL;

import org.hibernate.jpa.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.hibernate.jpa.boot.archive.internal.StandardArchiveDescriptorFactory;
import org.hibernate.jpa.boot.archive.spi.ArchiveDescriptor;

public class StandardArchiveDescriptor extends StandardArchiveDescriptorFactory{

    /**
     * Singleton access
     */
    public static final StandardArchiveDescriptor INSTANCE = new StandardArchiveDescriptor();

    @Override
    public ArchiveDescriptor buildArchiveDescriptor(URL url, String entry) {
        final String protocol = url.getProtocol();
        if ( "file".equals(protocol) && url.getFile().contains(".war!")) {
            return new JarProtocolArchiveDescriptor( this, url, entry );
        }
        return super.buildArchiveDescriptor(url, entry);
    }
}
