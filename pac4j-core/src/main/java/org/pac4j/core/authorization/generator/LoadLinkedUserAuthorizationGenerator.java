package org.pac4j.core.authorization.generator;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.profile.service.ProfileService;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableObject;

/**
 * Load a linked account and replace the original account.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class LoadLinkedUserAuthorizationGenerator extends InitializableObject
        implements AuthorizationGenerator {

    private ProfileService profileService;

    private boolean failIfLinkedUserNotFound = true;

    public LoadLinkedUserAuthorizationGenerator() {}

    public LoadLinkedUserAuthorizationGenerator(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    protected void internalInit() {
        CommonHelper.assertNotNull("profileService", profileService);
    }

    @Override
    public UserProfile generate(final WebContext context, final UserProfile profile) {
        init();

        final CommonProfile linkedProfile = profileService.findByLinkedId(profile.getId());

        if (linkedProfile != null) {
            return linkedProfile;
        } else {
            if (failIfLinkedUserNotFound) {
                throw new TechnicalException("No linked account found for: " + profile);
            } else {
                // fallback to the original account
                return profile;
            }
        }
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public void setProfileService(final ProfileService profileService) {
        this.profileService = profileService;
    }

    public boolean isFailIfLinkedUserNotFound() {
        return failIfLinkedUserNotFound;
    }

    public void setFailIfLinkedUserNotFound(final boolean failIfLinkedUserNotFound) {
        this.failIfLinkedUserNotFound = failIfLinkedUserNotFound;
    }

    @Override
    public String toString() {
        return CommonHelper.toNiceString(this.getClass(), "profileService", profileService,
            "failIfLinkedUserNotFound", failIfLinkedUserNotFound);
    }
}
