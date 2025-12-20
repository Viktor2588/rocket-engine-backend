# Deployment Guide - Render

This guide explains how to deploy the Rocket Engine Backend to Render.

## Prerequisites

- GitHub account with this repository
- Render account (free tier available at https://render.com)

## Deployment Steps

### Step 1: Push to GitHub

First, commit and push all changes to your GitHub repository:

```bash
git add .
git commit -m "Add Dockerfile, render.yaml, and production configuration for Render deployment"
git push origin main
```

### Step 2: Connect Render to GitHub

1. Go to https://dashboard.render.com
2. Click "New +" and select "Web Service"
3. Click "Connect Repository" and authorize GitHub
4. Search for your `rocket-engine-backend` repository
5. Click "Connect"

### Step 3: Configure Web Service

On the deployment screen, configure:

- **Name**: `rocket-engine-backend`
- **Environment**: `Docker`
- **Dockerfile Path**: `./Dockerfile` (auto-filled)
- **Build Command**: (leave empty - uses Dockerfile)
- **Start Command**: (leave empty - uses Dockerfile)

### Step 4: Set Environment Variables

Click "Advanced" and add these environment variables:

**For Database (Render will provide these automatically, but you'll need to set them):**

After you create the PostgreSQL database (Step 5), you'll get a `DATABASE_URL`. Set:

- **Key**: `SPRING_DATASOURCE_URL`
- **Value**: (from the PostgreSQL service's DATABASE_URL, e.g., `postgresql://user:pass@hostname:port/dbname`)

- **Key**: `SPRING_DATASOURCE_USERNAME`
- **Value**: `postgres` (or your custom username)

- **Key**: `SPRING_DATASOURCE_PASSWORD`
- **Value**: (your PostgreSQL password)

**For CORS:**

- **Key**: `CORS_ALLOWED_ORIGINS`
- **Value**: `https://viktor2588.github.io`

- **Key**: `SPRING_PROFILES_ACTIVE`
- **Value**: `production`

### Step 5: Create PostgreSQL Database

1. From the Render dashboard, click "New +" and select "PostgreSQL"
2. Configure:
   - **Name**: `rocket-engine-postgres`
   - **Database**: `rocket_engine_comparison`
   - **User**: `postgres`
   - **Region**: (choose one close to you)
   - **Version**: 16
   - **Plan**: Free

3. Click "Create Database"
4. Once created, copy the connection string from the database dashboard
5. Extract the username, password, and hostname from the connection string

### Step 6: Update Web Service with Database Details

Go back to your web service and update the environment variables with the actual database credentials from Step 5.

### Step 7: Deploy

1. Click "Create Web Service"
2. Render will automatically start building and deploying your application
3. You'll see deployment logs in real-time
4. Once deployment is complete, you'll get a URL like: `https://rocket-engine-backend.onrender.com`

## Post-Deployment

### Test Your API

Once deployed, test your API:

```bash
# Get all engines
curl https://rocket-engine-backend.onrender.com/api/engines

# The response should show your engines data
```

### Update Your Frontend

Update your frontend's API base URL to point to your Render backend:

```javascript
const API_BASE_URL = 'https://rocket-engine-backend.onrender.com';
```

### Important Notes

- **Cold Starts**: Render's free tier may have cold starts (5-10 seconds of inactivity). This is normal.
- **Uptime**: The free PostgreSQL database has a limited uptime guarantee. For production use, consider upgrading.
- **Auto-Deploy**: The `render.yaml` is configured to auto-deploy on push. Every time you push to main, it will automatically redeploy.

## Troubleshooting

### Database Connection Errors

If you see database connection errors:
1. Verify DATABASE_URL is correctly set in environment variables
2. Ensure the PostgreSQL database service is running (check Render dashboard)
3. Check the deploy logs for specific error messages

### CORS Errors

If your frontend can't access the API:
1. Verify `CORS_ALLOWED_ORIGINS` is set correctly to your frontend URL
2. Check browser console for CORS error messages
3. Ensure API requests include proper headers

### Build Failures

If the build fails:
1. Check the deployment logs for error messages
2. Ensure `Dockerfile` is at the root of your repository
3. Verify `gradlew` has execute permissions: `chmod +x gradlew`
4. Run local build: `./gradlew clean build` to test

## Monitoring

To view logs and monitor your application:
1. Go to your Web Service on Render dashboard
2. Click the "Logs" tab to see real-time logs
3. Set up error alerts in the "Settings" tab

## Further Help

- Render Documentation: https://render.com/docs
- Spring Boot Deployment: https://spring.io/guides/gs/deploying-spring-boot-app-to-cloud/
